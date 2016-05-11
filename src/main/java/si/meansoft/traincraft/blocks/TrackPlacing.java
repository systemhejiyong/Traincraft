package si.meansoft.traincraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import si.meansoft.traincraft.tileEntities.TileEntityRail;

import java.util.ArrayList;
import java.util.List;

import static si.meansoft.traincraft.blocks.BlockRail.FACING;

/**
 * @author canitzp
 */
public class TrackPlacing {

    public static void placeTrack(World world, EntityPlayer player, BlockPos pos, IBlockState state, BlockRail.TrackLength length, BlockRail.TrackDirection direction){
        EnumFacing facing = player.getHorizontalFacing();
        world.setBlockState(pos, state.withProperty(FACING, facing.getOpposite()), 2);
        switch(direction){
            case STRAIGHT:{
                if(canPlaceTrack(world, pos, length, facing)){
                    placeStraight(world, player, pos, state, length, facing);
                }
            }
        }
    }

    public static boolean canPlaceTrack(World world, BlockPos pos, BlockRail.TrackLength length, EnumFacing facing){
        switch(facing){
            case NORTH:{
                for(int i = 0; i < length.lenght; i++){
                    BlockPos newPos = pos.add(0, 0, -i);
                    Block block = world.getBlockState(newPos).getBlock();
                    if(!(pos.equals(newPos) && block instanceof BlockRail) && !(block == Blocks.AIR)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static void placeStraight(World world, EntityPlayer player, BlockPos pos, IBlockState state, BlockRail.TrackLength length, EnumFacing facing){
        switch (facing){
            case NORTH:{
                List<BlockPos> posList = new ArrayList<BlockPos>();
                for(int i = 0; i < length.lenght; i++){
                    posList.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - i));
                }
                for(BlockPos setPos : posList){
                    world.setBlockState(setPos, state.withProperty(FACING, facing.getOpposite()));
                    TileEntityRail te = (TileEntityRail) world.getTileEntity(setPos);
                    if(te != null){
                        te.harvestPositions.addAll(posList);
                    }
                }
            }
        }
    }

    public static void harvestTrack(World world, BlockPos pos, IBlockState state, TileEntityRail te){
        for(BlockPos pos1 : te.harvestPositions){
            world.setBlockToAir(pos1);
        }
    }

}
