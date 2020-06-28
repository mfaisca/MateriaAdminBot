package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ardyn extends _Default{
	public Ardyn() {
		super("Ardyn", "izunia", "izunia");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{9535};
				break;
			case HP:
				ids = new int[]{9533};
				break;
			case S1:
				ids = new int[]{9172};
				break;
			case S2:
				ids = new int[]{9176};
				break;
			case EX:
				ids = new int[]{9190, 9189, 9191};
				break;
			case AA:
				ids = new int[]{9199};
				break;
			case LD:
				ids = new int[]{9194};
				break;
			case BT:
				ids = new int[]{9195};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}